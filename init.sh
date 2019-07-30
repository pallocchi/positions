#!/bin/bash

TOKEN=$1

BASE_PATH='http://localhost:8080'

if [[ -z "$TOKEN" ]]
then
    echo "Missing token!"
    exit 1
fi

# Add clients

echo "️> Adding clients..."

curl -X POST "$BASE_PATH/clients" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name": "Green Future"}' && echo

curl -X POST "$BASE_PATH/clients" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name": "Von Misses"}'  && echo

curl -X POST "$BASE_PATH/clients" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name": "Free To Choose"}' && echo

# Add Hunts for clients

echo "> Adding hunts to clients..."

curl -X POST "$BASE_PATH/clients/1/hunts" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"location": "San Francisco", "name": "Java"}' && echo

curl -X POST "$BASE_PATH/clients/2/hunts" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name": "Data Scientist"}' && echo

curl -X POST "$BASE_PATH/clients/3/hunts" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"location": "New York", "type": "Full Time"}' && echo

# Add Github as a provider

echo "> Adding hunts to providers..."

curl -X POST "$BASE_PATH/providers" -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"key": "GITHUB", "maxPositions": 100, "name": "Github", "url": "https://jobs.github.com/positions.json?page={page}"}' && echo

# Import positions from Github

echo "> Import positions from Github..."

curl -X POST "$BASE_PATH/positions?provider=GITHUB" -H "Authorization: Bearer $TOKEN"
