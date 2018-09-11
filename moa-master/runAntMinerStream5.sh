#!/bin/bash

CAMP="/usr/bin/time -f %e nice java -d64  -cp ../moa.jar moa.DoTask"

DATASET_DIR=../DataStream

for i in $( ls $DATASET_DIR ); do

        PATTERN=`ls $DATASET_DIR/$i/*.arff`
	echo $PATTERN
        PATTERN=`echo $PATTERN | cut -d '/' -f4`
        echo $PATTERN
        for j in {1..1}; do

                SUFFIX="$j"

                if [ "$j" -lt "10" ]
                then
                        SUFFIX="0$j"
                fi
		
		$CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble ) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  (ArffFileStream -f $DATASET_DIR/$i/$PATTERN) -S"  >> $i-$SUFFIX.out 2>&1
		
        done

done

DATASET="SEAGenerator" 
for j in {1..1}; do
	
              SUFFIX="$j"
              if [ "$j" -lt "10" ]
              then
                       SUFFIX="0$j"
               fi
		
	      $CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  generators.SEAGenerator -S -i 1000000"  >> $DATASET-$SUFFIX.out 2>&1
		
 done

DATASET="RTGenerator" 
for j in {1..1}; do

              SUFFIX="$j"
              if [ "$j" -lt "10" ]
              then
                       SUFFIX="0$j"
               fi
		
		$CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  (generators.RandomTreeGenerator -c 3) -S -i 1000000"  >> $DATASET-$SUFFIX.out 2>&1
		
 done

DATASET="RTGenerator2" 
for j in {1..1}; do

              SUFFIX="$j"
              if [ "$j" -lt "10" ]
              then
                       SUFFIX="0$j"
               fi
		
		$CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  (generators.RandomTreeGenerator -c 3 -o 0 -u 4) -S -i 1000000"  >> $DATASET-$SUFFIX.out  2>&1
		
 done

DATASET="LEDGenerator" 
for j in {1..1}; do

              SUFFIX="$j"
              if [ "$j" -lt "10" ]
              then
                       SUFFIX="0$j"
               fi
		
		$CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  (generators.LEDGenerator) -S -i 1000000"  >> $DATASET-$SUFFIX.out  2>&1
		
 done

DATASET="HyperplaneGenerator" 
for j in {1..1}; do

              SUFFIX="$j"
              if [ "$j" -lt "10" ]
              then
                       SUFFIX="0$j"
               fi
		
		$CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  (generators.HyperplaneGenerator -k 0 -s 0) -S -i 1000000"  >> $DATASET-$SUFFIX.out  2>&1
		
 done

DATASET="RandomRBFGenerator" 
for j in {1..1}; do

              SUFFIX="$j"
              if [ "$j" -lt "10" ]
              then
                       SUFFIX="0$j"
               fi
		
		$CAMP "EvaluatePrequentialCV -l (moa.classifiers.rules.antminerarchive.AntMinerEnsumble) -a Bootstrap-Validation -e AdwinClassificationPerformanceEvaluator -s  (generators.RandomRBFGenerator) -S -i 1000000"  >> $DATASET-$SUFFIX.out  2>&1
		
 done

