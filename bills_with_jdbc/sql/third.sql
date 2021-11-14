-- За каждый день для каждого товара рассчитать количество и сумму 
-- полученного товара в указанном периоде, посчитать итоги за период
SELECT b.date as day,
       m.id as id,
       sum(quantity)::int as total_quantity,
       sum(price * quantity)::int as total_price
FROM bill_items
LEFT JOIN merch m on bill_items.merch_id = m.id
LEFT JOIN bills b on b.id = bill_items.bill_id
WHERE b.date BETWEEN ?::date and ?::date
GROUP BY m.id, b.date