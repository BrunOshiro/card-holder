local-env-create:
	docker-compose -f stack.yaml up -d
	docker-compose -f stack.yaml build cardholderapp
	sleep 5
	docker cp data/ddl.sql postgres:/var/lib/postgresql/data
	docker exec postgres psql -h localhost -U admin -d postgres -a -f ./var/lib/postgresql/data/ddl.sql

local-env-destroy:
	docker-compose -f stack.yaml down