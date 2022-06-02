package io.github.ilnurnasybullin.tagfm.core.api.dto;

import io.github.ilnurnasybullin.tagfm.api.service.SynonymTagManagerService;
import io.github.ilnurnasybullin.tagfm.core.model.synonym.SynonymTagManager;

import java.util.*;

/**
 * @author Ilnur Nasybullin
 */
public sealed interface SynonymTagManagerView extends SynonymTagManagerService<TagView> permits SynonymTagManager {
    <S extends SynonymGroupView> List<S> synonymGroups();
    <T extends TagView, S extends SynonymGroupView> Map<T, S> synonymMap();
}
