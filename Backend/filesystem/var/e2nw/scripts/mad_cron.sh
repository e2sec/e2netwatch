#!/bin/sh
/var/e2nw/scripts/mad.rb --new=/tmp/mad.txt --backtrace=1 --event --plain
/var/e2nw/scripts/event2anomaly.rb
