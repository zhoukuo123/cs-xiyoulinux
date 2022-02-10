select uid as uid,
       sno as sno,
       password as password,
       name as name,
       class_name as className,
       mobile as mobile,
       round as round,
       status as status,
       created_time as created_time,
       updated_time as updated_time
from join_info
where updated_time >= :sql_last_value
