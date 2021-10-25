NS3DIR="$HOME/ns-allinone-3.29/ns-3.29"

function waff {
    CWD="$PWD"
    cd $NS3DIR >/dev/null
    ./waf --cwd="$CWD" "$@"
    cd - >/dev/null
}

waff --run="Q1 --type=TcpNewReno"
waff --run="Q1 --type=TcpHighSpeed"
waff --run="Q1 --type=TcpVeno"
waff --run="Q1 --type=TcpVegas"
