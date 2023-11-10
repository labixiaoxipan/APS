package com.benewake.system.aspect;

import com.benewake.system.annotation.Scheduling;
import com.benewake.system.entity.enums.TableVersionState;
import com.benewake.system.exception.BeneWakeException;
import com.benewake.system.redis.DistributedLock;
import com.benewake.system.utils.HostHolder;
import com.benewake.system.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.benewake.system.redis.SchedulingLockKey.*;

@Aspect
@Component
public class SchedulingAspect {

    @Autowired
    private DistributedLock distributedLock;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private HostHolder hostHolder;

    @Around("@annotation(scheduling)")
    public Object doBefore(ProceedingJoinPoint point, Scheduling scheduling) throws Throwable {
        String username = redisTemplate.opsForValue().get(SCHEDULING_USER_LOCK_KEY);
        if (Objects.equals(username, hostHolder.getUser().getUsername())) {
            TableVersionState type = scheduling.type();
            try {
                if (distributedLock.acquireLock(SCHEDULING_DATA_LOCK_KEY, type.getDescription() ,20, TimeUnit.MINUTES)) {
                    return point.proceed();
                } else {
                    String ing = redisTemplate.opsForValue().get(SCHEDULING_DATA_LOCK_KEY);
                    throw new BeneWakeException(ing + "请稍后");
                }
            } finally {
                if (!type.equals(TableVersionState.SCHEDULING_ING)) {
                    distributedLock.releaseLock(SCHEDULING_DATA_LOCK_KEY, type.getDescription());
                }
            }
        } else {
            if (StringUtils.isEmpty(username)) {
                throw new BeneWakeException("当前无人使用 请关闭网页重新获取所有权");
            }
            throw new BeneWakeException("当前" + username + "正在使用！");
        }
    }
}