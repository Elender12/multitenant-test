DO $$
BEGIN

IF NOT EXISTS (
	SELECT 1
	FROM information_schema.tables
	WHERE  table_schema = 'sample'
	AND table_name = 'district'
) THEN

CREATE TABLE sample.district
(
	id UUID PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	modified DATE NOT NULL,
	created DATE  NOT NULL
);

END IF;

END;
$$;