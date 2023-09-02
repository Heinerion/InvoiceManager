UPDATE address a
SET name = a.recipient;

-- E in front of '\n' interprets the escape sequence
UPDATE address a
SET block = concat( a.recipient , E'\n'
    , a.company , E'\n'
    , a.district , E'\n'
    , a.street , ' ' , a.number , E'\n'
    , a.apartment , E'\n'
    , a.postal_code , ' ' , a.location );