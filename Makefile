# Top-level shortcut. Real targets live in backend/Makefile.

.PHONY: dev migrate test lint backend-install android-build

dev:
	$(MAKE) -C backend dev

migrate:
	$(MAKE) -C backend migrate

test:
	$(MAKE) -C backend test

lint:
	$(MAKE) -C backend lint

backend-install:
	cd backend && npm install

android-build:
	cd app && ./gradlew assembleDebug
