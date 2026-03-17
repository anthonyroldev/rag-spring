#!/bin/sh
ollama serve &
sleep 5
ollama pull qwen3-embedding:4b
wait