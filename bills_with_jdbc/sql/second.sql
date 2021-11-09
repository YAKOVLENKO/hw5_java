-- Выбрать поставщиков с суммой поставленного товара выше указанного количества 
-- (товар и его количество должны допускать множественное указание).

SELECT id_o as id
FROM (
  SELECT o.id          as id_o,
         m.id          as id_m,
         sum(quantity) as total_quantity
  FROM bill_items
           LEFT JOIN merch m on m.id = bill_items.merch_id
           LEFT JOIN bills b on b.id = bill_items.bill_id
           LEFT JOIN organizations o on o.id = b.sender_id
  GROUP BY o.id, m.id
) as org_info
INNER JOIN (
    SELECT unnest(?::int[]) as merch_id,
       unnest(?::int[]) as least_number
    ) as cond
ON id_m = merch_id
WHERE total_quantity > least_number
GROUP BY id_o
HAVING ARRAY_AGG(merch_id) @> ?::int[]