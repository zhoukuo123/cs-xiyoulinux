select ji.uid        as uid,
       ji.sno        as sno,
       ji.name       as name,
       ji.class_name as className,
       ji.mobile     as mobile,
       jr.grade      as grade,
       ji.round      as round,
       ji.status     as status,
       ji.created_time as created_time,
       ji.updated_time as updated_time
from cs_join_admin.join_info ji
         left join cs_join_admin.join_record jr on ji.uid = jr.uid and ji.round = jr.round
where ji.updated_time >= :sql_last_value
order by jr.grade desc