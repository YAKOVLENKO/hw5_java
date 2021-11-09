-- Вывести список товаров, поставленных организациями за период. 
-- Если организация товары не поставляла, то она все равно должна быть отражена в списке.
SELECT id,
       COALESCE(id_merch, 0) as merch_id
FROM organizations
LEFT JOIN (
    SELECT m.id   as id_merch,
           sender_id
    FROM bill_items
             LEFT JOIN bills b on b.id = bill_items.bill_id
             LEFT JOIN merch m on m.id = bill_items.merch_id
    WHERE date between ?::date AND ?::date
    GROUP BY sender_id, m.id
) as mrch
ON id = mrch.sender_id