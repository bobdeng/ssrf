package com.github.bobdeng.ssrf.forms;

import com.github.bobdeng.ssrf.annotations.Param;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by zhiguodeng on 2016/12/13.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFormWithByteArray {
    @Param("name")
    private String name;
    private String[] tags;
    private byte[] avatar;
    private byte[][] album;
}
