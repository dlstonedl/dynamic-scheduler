# dynamic-scheduler
基于**quartz**二次开发，实现动态配置定时任务，此外会新增部分**quartz**未提供的特性。
应用接口文档由**swagger**自动生成，可通过**swagger-ui**查看。

### 主要特性
- 查询所有任务
- 任务基本操作：添加，修改，删除；支持配置POST(url), cron表达式
- 任务运行时操作：立即执行(EXECUTE)，暂停(PAUSE), 从暂停中恢复(RESUME)
- 新增特性: 任务历史执行信息查询(最近10条)

### 示例：新增任务
```
# POST http://localhost:8080/quartz/jobs
{
	"jobName": "job-hello",
	"jobGroupName": null,
	"jobDescription": "job hello",
	"url": "http://localhost:8888/hello?name=111",
	"cronExpression": "0/1 * * * * ?"
}
```

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
