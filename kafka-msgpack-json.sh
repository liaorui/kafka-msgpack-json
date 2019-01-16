#!/bin/bash

exec $(dirname $0)/kafka-run-class.sh cn.learning.ConsoleConsumer "$@"
