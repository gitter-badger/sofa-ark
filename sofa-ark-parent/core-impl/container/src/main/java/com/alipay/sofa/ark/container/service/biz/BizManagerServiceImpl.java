/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.ark.container.service.biz;

import com.alipay.sofa.ark.exception.ArkException;
import com.alipay.sofa.ark.spi.model.Biz;
import com.alipay.sofa.ark.spi.service.biz.BizManagerService;
import com.google.inject.Singleton;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  Service Implementation to manager ark biz
 *
 * @author ruoshan
 * @since 0.1.0
 */
@Singleton
public class BizManagerServiceImpl implements BizManagerService {

    private ConcurrentHashMap<String, Biz> bizs = new ConcurrentHashMap<>();

    @Override
    public void registerBiz(Biz biz) {
          if (bizs.putIfAbsent(biz.getBizName(), biz) != null){
              throw new ArkException(String.format("duplicate biz: %s exists.", biz.getBizName()));
          }
    }

    @Override
    public Biz getBizByName(String bizName) {
       return bizs.get(bizName);
    }

    @Override
    public Set<String> getAllBizNames() {
        return bizs.keySet();
    }

    @Override
    public List<Biz> getBizsInOrder() {
        List<Biz> bizList = new ArrayList<>();
        bizList.addAll(bizs.values());
        Collections.sort(bizList, new Comparator<Biz>() {
            @Override
            public int compare(Biz o1, Biz o2) {
                return Integer.compare(o1.getPriority(), o2.getPriority());
            }
        });
        return bizList;
    }
}