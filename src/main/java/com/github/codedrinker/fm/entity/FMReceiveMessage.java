/*
 * Copyright 2017 Chunlei Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.codedrinker.fm.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.util.List;

@Data
public class FMReceiveMessage {

    private String object;

    private List<Entry> entry;

    @Data
    public static class Entry {

        private String id;
        private long time;
        private String uid;
        private List<Change> changes;
        private List<Change> changed_fields;//change 只有 field 字段有内容
        private List<Messaging> messaging;
        /**
         * https://developers.facebook.com/docs/messenger-platform/reference/webhook-events/standby
         * 备用 信息通道，不同于 messaging
         */
        private List<Messaging> standby;

        /**
         * 通过正常 messaging 字段获取消息。如果不存在，尝试通过备用通道获取
         */
        public List<Messaging> getMessaging() {
            if (messaging != null) {
                System.out.println("Entry => messaging =>" + JSON.toJSONString(messaging));
                return messaging;
            }
            System.out.println("Entry => standby =>" + JSON.toJSONString(standby));
            return standby;
        }
    }

    /**
     * 用于记录 订阅的内容变化情况
     */
    @Data
    public static class Change {
        public String field;//变化的字段
        public String value;//变化的字段值
    }

    @Data
    public static class Messaging {

        private Member sender;
        private Member recipient;
        private long timestamp;
        private Message message;
        private Delivery delivery;
        private Read read;
        private PostBack postback;
        private Referral referral;

        @Data
        public static class Referral {
            private String ref;
            private String source;
            private String type;
        }

        @Data
        public static class PostBack {

            private String payload;
            private Referral referral;
        }

        @Data
        public static class Delivery {
            private List<String> mids;
            private long watermark;
            private long seq;
        }

        @Data
        public static class Read {
            private long watermark;
            private long seq;
        }

        @Data
        public static class Message {
            private Boolean is_echo;
            private String app_id;
            private String metadata;

            private String mid;
            private Integer seq;
            private String text;
            private QuickReply quick_reply;
            private List<Attachment> attachments;

            @Data
            public static class Attachment {
                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public Payload getPayload() {
                    return payload;
                }

                public void setPayload(Payload payload) {
                    this.payload = payload;
                }

                private String title;//title
                private String type;//image、audio、video、file 或 location
                private Payload payload;//multimedia 或 location 负载

                public static class Payload {
                    public String getUrl() {
                        return url;
                    }

                    public void setUrl(String url) {
                        this.url = url;
                    }

                    private String url;
                }
            }

            public static class QuickReply {
                public String getPayload() {
                    return payload;
                }

                public void setPayload(String payload) {
                    this.payload = payload;
                }

                private String payload;
            }

        }

        public static class Member {
            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String id;
        }
    }

}
