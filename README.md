# ComboView
直播送礼连击按钮

直播送礼连击功能

基本使用：<br/>
1.布局文件引用<br/>
```
  <com.example.eagleweb.combobutton.ComboTextView
            android:id="@+id/combo"
            android:layout_width="96dp"
            android:layout_height="30dp"
            android:layout_centerInParent="true"/>
 ```
 <br/>
2.Java代码创建对象
<br/>
方法参数：<br/>
```
    public void setComboListener(ComboListener comboListener): 设置连击监听


    public interface ComboListener {
        /**
         * 点击事件回调
         *
         * @param isCombo    该次点击是否是连击
         * @param comboCount 连击次数
         */
        void click(boolean isCombo, int comboCount);

        /**
         * 连击结束
         *
         * @param comboCount 连击次数
         */
        void comboOver(int comboCount);
    }

    /**
     * 设置按钮类型
     *
     * @param type 1：可连击  2：不可连击 (默认可连击)
     */
    public void setType(int type)
    
 ```
