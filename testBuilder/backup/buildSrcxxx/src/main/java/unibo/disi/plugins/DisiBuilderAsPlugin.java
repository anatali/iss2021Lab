package unibo.disi.plugins;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class DisiBuilderAsPlugin implements Plugin<Project>  {
    @Override
    public void apply(Project project) {
        project.getTasks().create("myBuildTask", MyTask.class);
    }

}
