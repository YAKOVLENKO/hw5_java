-- Рассчитать среднюю цену полученного товара за период

SELECT CASE WHEN tot_quantity != 0 THEN tot_price / tot_quantity
        ELSE 0 END as avg_price
FROM (
         SELECT sum((quantity * price) * good_date) as tot_price,
                sum(quantity * good_date)           as tot_quantity
         FROM (
                  SELECT COALESCE(quantity, 0) as quantity,
                         COALESCE(price, 0)    as price,
                         CASE
                             WHEN date between ?::date AND ?::date THEN 1
                             ELSE 0 END        as good_date,
                         merch.id              as mid
                  FROM merch
                           LEFT JOIN bill_items bi on merch.id = bi.merch_id
                           LEFT JOIN bills b on bi.bill_id = b.id
                  WHERE merch.id = ?
              ) as f1
         GROUP BY mid
     ) as ff1