/**
  * Copyright 2020 bejson.com
  */
package org.zju.vipa.aix.container.common.json;
import java.util.List;

/**
 * Auto-generated: 2020-08-10 14:34:7
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TaskArgs {

    private List<String> tasks;
    private Algorithms algorithms;
    private List<Datasets> datasets;
    private List<TeacherModels> teacher_models;
    private String student_models;
    public void setTasks(List<String> tasks) {
         this.tasks = tasks;
     }
     public List<String> getTasks() {
         return tasks;
     }

    public void setAlgorithms(Algorithms algorithms) {
         this.algorithms = algorithms;
     }
     public Algorithms getAlgorithms() {
         return algorithms;
     }

    public void setDatasets(List<Datasets> datasets) {
         this.datasets = datasets;
     }
     public List<Datasets> getDatasets() {
         return datasets;
     }

    public void setTeacher_models(List<TeacherModels> teacher_models) {
         this.teacher_models = teacher_models;
     }
     public List<TeacherModels> getTeacher_models() {
         return teacher_models;
     }

    public void setStudent_models(String student_models) {
         this.student_models = student_models;
     }
     public String getStudent_models() {
         return student_models;
     }

}
