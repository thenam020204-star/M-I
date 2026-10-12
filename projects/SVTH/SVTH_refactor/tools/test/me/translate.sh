#!/bin/bash
# translate.sh <in.txt> <out.txt> — dịch kịch bản MeRun từ tên gốc sang tên đã đặt tên (mapping/resolved.map, Phiên 5).
# Chỉ 2 kiểu tham chiếu tên làm rối xuất hiện trong kịch bản; kiểm bằng: grep ' -> false' trong out.txt khi chạy (setf/callm/probe báo không tìm thấy).
sed -e 's/^probe at b:boolean/probe DeviceConfig playMusic:boolean/' \
    -e 's/^callm da a:da a:cd -- b /callm GameCanvas instance:GameCanvas world:World -- startScriptBattle /' "$1" > "$2"
