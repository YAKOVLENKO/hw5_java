-- Выбрать первые 10 поставщиков по количеству поставленного товара

SELECT organizations.id as id
FROM organizations
LEFT JOIN bills b on organizations.id = b.sender_id
LEFT JOIN bill_items bi on b.id = bi.bill_id
LEFT JOIN merch m on bi.merch_id = m.id
WHERE merch_id = ? OR merch_id IS NULL
GROUP BY organizations.id
ORDER BY COALESCE(sum(quantity), 0) DESC
LIMIT 10