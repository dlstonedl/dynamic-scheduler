# dynamic-scheduler
动态配置定时任务

### swagger
```
# swagger-ui
http://localhost:8080/swagger-ui.html
# swagger-api
http://localhost:8080/v2/api-docs
```

### 注意
```
org.quartz.jobStore.misfireThreshold
默认60s，在resume Job时，可能会出现多次执行的情况
例如：JobA每5秒执行一次，pause JobA: 
1. 如果在60s之内resume JobA，此时JobA会执行多次; quartz将其当做延迟；
2. 如果间隔时间大于60S，resume JobA，quartz会调用updateAfterMisfire()，重新设置NextFireTime；
此配置设置过小，会导致正常的延迟Job频繁触发misfired，需慎重；
```
