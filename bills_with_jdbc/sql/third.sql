-- За каждый день для каждого товара рассчитать количество и сумму 
-- полученного товара в указанном периоде, посчитать итоги за период

SELECT day,
       id,
       COALESCE(tot_quantity,0) as total_quantity,
       COALESCE(tot_price,0) as total_price
FROM (
      SELECT generate_series(?::date, ?::date, '1 day') :: DATE AS day,
             id
      FROM merch
    ) m_info
LEFT JOIN (
    SELECT m.id as merch_id,
       sum(price * quantity)::int as tot_price,
       sum(quantity)::int as tot_quantity,
       b.date as merch_date
    FROM bill_items
    LEFT JOIN merch m on bill_items.merch_id = m.id
    LEFT JOIN bills b on b.id = bill_items.bill_id
    GROUP BY m.id, b.date
    ) as date_info
ON day = date_info.merch_date AND id = merch_id