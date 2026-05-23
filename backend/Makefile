.PHONY: dev migrate migrate-status test test-unit test-integration lint install

install:
	npm install

dev:
	node --watch src/server.js

migrate:
	node scripts/migrate.js up

migrate-status:
	node scripts/migrate.js status

test:
	NODE_OPTIONS=--experimental-vm-modules npx jest

test-unit:
	NODE_OPTIONS=--experimental-vm-modules npx jest --testPathPattern=services

test-integration:
	NODE_OPTIONS=--experimental-vm-modules npx jest --testPathPattern=integration

lint:
	npx eslint src/
