NS3DIR="$HOME/ns-allinone-3.29/ns-3.29"

function waff {
    CWD="$PWD"
    cd $NS3DIR >/dev/null
    ./waf --cwd="$CWD" "$@"
    cd - >/dev/null
}

waff --run="Q3 --config=1"
waff --run="Q3 --config=2"
waff --run="Q3 --config=3"
