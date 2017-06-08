package com.recognizer.common.widget.syllabus.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dongjun Zou
 * @Description ${todo}
 * @email 984147586@qq.com
 * @date 2017/5/7 02:05
 */

public class SyllabusDao {
    public static List<SimpleSection> getCourseData() {

        List<SimpleSection> sectionList = new ArrayList<SimpleSection>();

        // 初始化课程数据
        //ID、周几、课程名、教师、教室、开始节次、结束节次
        SimpleSection sections_1_1 = new SimpleSection(1, 1, "大学英语I", "杨鸿年", "B204", 1, 3);
        SimpleSection sections_1_2 = new SimpleSection(2, 1, "Java程序设计", "李念红", "I524", 9, 10);
        SimpleSection sections_2_1 = new SimpleSection(3, 2, "中国近代史", "李小霞", "D502", 1, 3);
        SimpleSection sections_2_2 = new SimpleSection(4, 2, "C程序设计与分析", "钟丽", "B213", 5, 6);
        SimpleSection sections_3_1 = new SimpleSection(5, 3, "数字电路技术基础", "熊晓霞", "E205", 3, 4);
        SimpleSection sections_4_1 = new SimpleSection(6, 4, "数字电路技术实验", "李小龙", "D401", 3, 4);
        SimpleSection sections_4_2 = new SimpleSection(7, 4, "石油工程概论", "曲丽", "B305", 5, 6);
        SimpleSection sections_4_3 = new SimpleSection(8, 4, "Web程序设计基础", "程琳", "D103", 7, 8);

        sectionList.add(sections_1_1);
        sectionList.add(sections_1_2);
        sectionList.add(sections_2_1);
        sectionList.add(sections_2_2);
        sectionList.add(sections_3_1);
        sectionList.add(sections_4_1);
        sectionList.add(sections_4_2);
        sectionList.add(sections_4_3);
        return sectionList;
    }
}
