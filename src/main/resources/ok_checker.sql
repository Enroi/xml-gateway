select *
from RESOURCE_REQUEST_DOCUMENT parent
where not exists (
select 1 from RESOURCE_REQUEST_DOCUMENT linked where parent.request_id = linked.request_id and linked.document_type = 'finished'
)
