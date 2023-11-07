/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mvcnplight.repository;

import com.example.mvcnplight.model.Device;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.example.mvcnplight.model.Device.DEVICE_ATTRIBUTE_KEY;


/**
 * Channel Repository using HashMap
 *
 * @author Jibeom Jung akka. Manty
 */

@Slf4j
@Data

public class ChannelRepository {
//    @Autowired
//    private  RtkRepository rtkRepository;
    private ConcurrentMap<String, Channel> channelCache = new ConcurrentHashMap<>();

    public void put(String key, Channel value) {
        channelCache.put(key, value);
        log.info("put " + key);
        log.info("channelCache size : " + channelCache.size());
        log.info(">> " + channelCache);
//        rtkRepository.put(key, value);


    }

    public Map<String,Channel> getChannelsByImeis(List<String> imeis){
      return channelCache.entrySet().stream()
                .filter(e->imeis.contains(e.getKey()))
               .collect(Collectors.toMap(k->k.getKey(),v->v.getValue()));
    }


    public Channel get(String key) {
        return channelCache.get(key);
    }

    public void remove(String key) {
        log.info("channelCache size : " + channelCache.size());
        this.channelCache.remove(key);
//        rtkRepository.remove(key);

    }

    public boolean remove(String key, Channel channel) {

        var result = this.channelCache.remove(key, channel);
        log.info("remove " + key);
        log.info("channelCache size : " + channelCache.size());
        log.info(">> " + channelCache);
//        rtkRepository.remove(key, channel);
        return result;


    }

    public List<Device> getAllDevices() {
        return channelCache.entrySet()
                .stream()
                .map(d -> d.getValue().attr(DEVICE_ATTRIBUTE_KEY).get())
                .collect(Collectors.toList());
    }

    public int size() {
        return this.channelCache.size();
    }
}
