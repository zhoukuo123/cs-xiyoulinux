select id as id,
       user_id as userId,
       activity_title as activityTitle,
       activity_content as activityContent,
       activity_end_time as activityEndTime,
       activity_status as activityStatus,
       activity_type as activityType,
       activity_create_time as activityCreateTime,
       activity_files as activityFiles,
       activity_update_time as activityUpdateTime
from cs_user_activity
where activity_update_time >= :sql_last_value
