package cn.v5.forms;

import cn.v5.annotations.Param;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {
    @Param("name")
    private String name;
    private String[] tags;
}
