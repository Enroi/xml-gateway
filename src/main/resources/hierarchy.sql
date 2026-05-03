WITH RECURSIVE document_chain(id, json_hash, level) as(
  SELECT
    ID,
    cast(json_hash as varchar),
    1 AS level
  FROM resource_request_document
  WHERE parent_document_hash IS NULL
  and request_id = '1'

  UNION ALL

  SELECT
    c.ID,
    cast(c.json_hash as varchar),
    p.level + 1
FROM
    resource_request_document c
INNER JOIN document_chain p ON
    cast(c.parent_document_hash as varchar) = p.json_hash
)
SELECT
    rd.request_id ,
    dc.level,
    rd.request_id,
    rd.document_type,
    rd.created_at
FROM
    document_chain dc
inner join resource_request_document rd on
    rd.id = dc.id
ORDER BY
    rd.request_id,
    level;
