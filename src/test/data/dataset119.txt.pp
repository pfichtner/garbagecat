Java HotSpot(TM) 64-Bit Server VM (24.79-b02) for linux-amd64 JRE (1.7.0_79-b15), built on Apr 10 2015 11:34:48 by "java_re" with gcc 4.3.0 20080428 (Red Hat 4.3.0-8)
Memory: 4k page, physical 32879568k(23231632k free), swap 50331644k(50219672k free)
CommandLine flags: -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:+CMSClassUnloadingEnabled -XX:+CMSCompactWhenClearAllSoftRefs -XX:+CMSConcurrentMTEnabled -XX:+CMSIncrementalMode -XX:CMSInitiatingOccupancyFraction=80 -XX:+CMSParallelRemarkEnabled -XX:+CMSScavengeBeforeRemark -XX:CodeCacheExpansionSize=65536 -XX:ConcGCThreads=3 -XX:+DisableExplicitGC -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/jboss/logs -XX:InitialHeapSize=23622320128 -XX:MaxHeapSize=23622320128 -XX:MaxNewSize=9663676416 -XX:MaxPermSize=4294967296 -XX:MaxTenuringThreshold=20 -XX:NewSize=9663676416 -XX:OldPLABSize=16 -XX:ParGCCardsPerStrideChunk=12288 -XX:ParallelGCThreads=8 -XX:PermSize=2147483648 -XX:+PrintGC -XX:+PrintGCCause -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:ReservedCodeCacheSize=134217728 -XX:StringTableSize=2000 -XX:SurvivorRatio=7 -XX:+UnlockDiagnosticVMOptions -XX:+UnlockExperimentalVMOptions -XX:-UseBiasedLocking -XX:+UseCMSInitiatingOccupancyOnly -XX:+UseCompressedOops -XX:+UseConcMarkSweepGC -XX:+UseFastAccessorMethods -XX:+UseParNewGC
2017-01-08T14:18:15.878+0300: 58626.878: [GC (GCLocker Initiated GC)2017-01-08T14:18:15.878+0300: 58626.878: [ParNew: 5908427K->5908427K(8388608K), 0.0000320 secs] 19349630K->19349630K(22020096K) icms_dc=100 , 0.0002560 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]
GC locker: Trying a full collection because scavenge failed
2017-01-08T14:18:15.879+0300: 58626.878: [Full GC (GCLocker Initiated GC)2017-01-08T14:18:15.879+0300: 58626.878: [CMS (concurrent mode failure): 13441202K->12005469K(13631488K), 23.1836190 secs] 19349630K->12005469K(22020096K), [CMS Perm : 1257346K->1257346K(2097152K)] icms_dc=100 , 23.1838500 secs] [Times: user=22.77 sys=0.39, real=23.18 secs]