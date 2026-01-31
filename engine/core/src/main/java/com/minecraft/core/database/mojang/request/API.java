package com.minecraft.core.database.mojang.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public abstract class API {

    private String URL;

    public abstract UUID request(String name);
}
