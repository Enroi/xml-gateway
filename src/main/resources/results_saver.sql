call CSVWRITE('output2.csv', '
select request_id as "Request ID", TO_CHAR(min(created_at), ''MI:SS.FF'') as "STARTED AT", TO_CHAR(min(created_at), ''MI:SS'') as "STARTED AT SECONDS ONLY", to_char(max(created_at), ''MI:SS.FF'') as "FINISHED AT", DATEDIFF(''MILLISECOND'', min(created_at), max(created_at)) as "DURATION"
from RESOURCE_REQUEST_DOCUMENT
group by request_id
order by 2
')
