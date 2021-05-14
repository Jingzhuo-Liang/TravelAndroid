package com.example.ljzpopupwindowlibrary;

/**
 * @@author:ljz
 * @@date:2021/5/14,11:08
 * @@version:1.0
 * @@annotation:
 **/
public class PopUpWindowItem {
        /** 图标 */
        private int icon = 0;
        /** 文字 */
        private String text;
        /** 文字、图标 颜色 */
        private int color = 0x888888;

        public PopUpWindowItem(){

        }

        public PopUpWindowItem(int i, String t, int c){
            icon = i;
            text = t;
            color = c;
        }

        public int getIcon() {
            return icon;
        }

        public String getText() {
            return text;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
}
