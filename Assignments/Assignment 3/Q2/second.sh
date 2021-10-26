NS3DIR="$HOME/ns-allinone-3.29/ns-3.29"

function waff {
    CWD="$PWD"
    cd $NS3DIR >/dev/null
    ./waf --cwd="$CWD" "$@"
    cd - >/dev/null
}

for rate in 2 4 10 20 50
do
    waff --run="Q2 --type=1 --rate=$rate"
done
for rate in 0.5 1 2 4 10
do
    waff --run="Q2 --type=2 --rate=$rate"
done
