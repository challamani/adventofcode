package com.adventofcode.aoc2022.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("day7")
@Slf4j
public class Day7 implements Puzzle<String,Integer> {

    private final Map<Pattern,String> pattenAction;

    @Autowired
    public Day7(){
        pattenAction = new HashMap<>();
        pattenAction.put(Pattern.compile("\\$ cd \\/$"),"ROOT");
        pattenAction.put(Pattern.compile("\\$ ls$"),"LIST_ITEMS");
        pattenAction.put(Pattern.compile("dir (.+)$"),"NEW_DIR");
        pattenAction.put(Pattern.compile("\\$ cd (.+)$"),"SWITCH_DIR");
        pattenAction.put(Pattern.compile("(\\d+) (.+)$"),"FILE_FOUND");
        pattenAction.put(Pattern.compile("\\$ cd \\.\\.$"),"GO_BACKWARD");
    }

    @Override
    public Integer solve(String input) {
        String current_dir="ROOT";
        Map<String,Integer> dirSize = new HashMap<>();

        for(String record: input.split("\\n")){

            Optional<Pattern> foundPattern  = pattenAction.keySet().stream().filter(pattern -> {
                    Matcher matcher =  pattern.matcher(record);
                    return matcher.find();
            }).findFirst();

            if(foundPattern.isPresent()){
                Pattern pattern = foundPattern.get();
                Matcher matcher = pattern.matcher(record);
                matcher.find();

                switch (pattenAction.get(pattern)) {
                    case "ROOT":
                        dirSize.put("ROOT", 0);
                        break;
                    case "NEW_DIR":
                        String new_dir =  matcher.group(1);
                        dirSize.putIfAbsent(current_dir.concat(".").concat(new_dir), 0);
                        break;
                    case "LIST_ITEMS":
                        //no-action
                        break;
                    case "SWITCH_DIR":
                        String switch_dir =  matcher.group(1);
                        current_dir = current_dir.concat(".").concat(switch_dir);
                        dirSize.putIfAbsent(current_dir, 0);
                        break;
                    case "GO_BACKWARD":
                        current_dir = current_dir.substring(0,current_dir.lastIndexOf("."));
                        break;
                    case "FILE_FOUND":
                        String fileSize = matcher.group(1);
                        dirSize.putIfAbsent(current_dir, 0);
                        int fSize = Integer.parseInt(fileSize);
                        dirSize.put(current_dir, dirSize.get(current_dir) + fSize);
                        cascadeValueToParents(dirSize,current_dir,fSize);
                        break;
                }
            }
        }

        int rootSize = dirSize.get("ROOT");
        int unusedSpace = 70000000 - rootSize;
        log.info("current unused space {} and desired-space {} required-space {}",unusedSpace, 30000000,(30000000 - unusedSpace));
        int requiredSpace = (30000000 - unusedSpace);

        AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
        dirSize.entrySet().stream().forEach(stringIntegerEntry -> {
            if(requiredSpace <= stringIntegerEntry.getValue() && min.get() > stringIntegerEntry.getValue()){
                min.set(stringIntegerEntry.getValue());
            }
        });
        
        //log.info("dirSize {}",dirSize);

        /** part1
        return dirSize.values().stream().filter(size -> size <= 100000)
                .mapToInt(Integer::intValue).sum();
         */
        return min.get();
    }

    private void cascadeValueToParents(Map<String,Integer> dirSize,String current_dir,int size) {
        String key = current_dir;
        int lastIndex = key.lastIndexOf(".");
        while (lastIndex != -1) {
            key = key.substring(0, lastIndex);
            dirSize.putIfAbsent(key, 0);
            dirSize.put(key, dirSize.get(key) + size);
            lastIndex = key.lastIndexOf(".");
        }
    }
}
