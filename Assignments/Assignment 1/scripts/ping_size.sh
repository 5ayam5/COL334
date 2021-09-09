cmd="/bin/ping $1 -W1 -c2 -s"
l=1
rt=$((1<<32))
M=-1
while [ $l -le $rt ]
do
    mid=$(($l+$rt))
    mid=$((mid>>1))
    $cmd$mid
    if [ $? -eq 0 ]
    then
        M=$mid
        l=$(($mid+1))
    else
        rt=$(($mid-1))
    fi
    echo ""
    echo ""
done
echo "The max size of ping for $1 is $M bytes"

