CREATE OR REPLACE FUNCTION public.cuentaJuegos()
RETURNS int
	LANGUAGE plpgsql
AS $$
declare
   contador integer;
	BEGIN
		select count(*) into contador from juego;
        return contador;
	END;
$$;
