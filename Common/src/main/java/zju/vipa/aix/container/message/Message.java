package zju.vipa.aix.container.message;

/**
 * @Date: 2020/1/9 21:59
 * @Author: EricMa
 * @Description: 容器与平台通信的消息体
 */
public class Message {
        /** 结束字符串 */
        public static final String END_STRING = "EOF-AIX-MESSAGE";
        /** 消息字符编码格式 */
        public static final String CHARSET_NAME = "UTF-8";

        private Intent intent;
        private String value;

        public Message(Intent intent, String value) {
                this.intent = intent;
                this.value = value;
        }

        public Message(Intent intent) {
                this.intent = intent;
                this.value = "";
        }


        public Intent getIntent() {
                return intent;
        }

        public void setIntent(Intent intent) {
                this.intent = intent;
        }

        public String getValue() {
                return value;
        }

        public void setValue(String value) {
                this.value = value;
        }

        @Override
        public String toString() {
                return "{" +
                    "intent='" + intent + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
}
