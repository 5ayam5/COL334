cmd="/bin/ping $1 -c1 -s1 -t"
l=1
rt=255
m=-1
while [ $l -le $rt ]
do
    mid=$(($l+$rt))
    mid=$((mid>>1))
    echo "TTL: $mid"
    $cmd$mid
    if [ $? -eq 0 ]
    then
        m=$mid
        rt=$(($mid-1))
    else
        l=$(($mid+1))
    fi
    echo ""
    echo ""
done
echo "The min TTL of ping for $1 is $m hops"

