create function getotchet(begin_date timestamp without time zone, end_date timestamp without time zone, OUT character varying, OUT character varying, OUT character varying, OUT integer, OUT integer, OUT integer, OUT integer) returns record
    language sql
as
$$
select s.url, s.namespace, s.author, count(t.label), count(s2.service_code),
               count(sc.name), count(s.doc_code)
        from store s
                 join type t on s.type_id = t.id
                 join service s2 on s2.id = s.service_id
                 join status_code sc on sc.id = s.status_id
        where s.created between begin_date AND end_date
        group by s.author, s.url, s.namespace
        order by s.url, s.author DESC;
$$;

alter function getotchet(timestamp, timestamp, out varchar, out varchar, out varchar, out integer, out integer, out integer, out integer) owner to postgres;

