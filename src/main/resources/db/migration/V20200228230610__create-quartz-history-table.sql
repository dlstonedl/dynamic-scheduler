CREATE TABLE job_history(
  id bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  job_name VARCHAR(190) NOT NULL,
  job_group VARCHAR(190) NOT NULL,
  trigger_name VARCHAR(190) NOT NULL,
  trigger_group VARCHAR(190) NOT NULL,
  job_start_time datetime,
  job_end_time datetime,
  job_duration BIGINT(13),
  job_status VARCHAR(10),
  job_exception VARCHAR(8000),
  PRIMARY KEY (ID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
