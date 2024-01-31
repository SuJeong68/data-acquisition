package com.nhnacademy.aiotdevicegateway;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.nhnacademy.aiotdevicegateway.exception.NotExistsNodeException;
import com.nhnacademy.aiotdevicegateway.node.NodeGroup;
import com.nhnacademy.aiotdevicegateway.node.NodeInfo;
import com.nhnacademy.aiotdevicegateway.node.base.ActiveNode;
import com.nhnacademy.aiotdevicegateway.node.base.InAble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

/**
 * settings/nodes.json 리소스를 읽어 각 Node를 생성하고,
 * 생성된 Node들을 연결하고 실행합니다.
 *
 * @author 이수정
 */
@Slf4j
public class NodeMaker {

    private NodeMaker() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * settings/nodes.json 리소스를 읽어 각 Node를 생성해 NODES Map에 저장합니다.
     * node간 wire를 연결하고 node를 실행시킵니다.
     *
     * @author 이수정
     */
     public static void makeNodes() {
        try {
            ClassPathResource resource = new ClassPathResource("settings/nodes.json");
            JsonArray jsonArray = new Gson().fromJson(Files.readString(resource.getFile().toPath()), JsonArray.class);

            for (int i = 0; i < jsonArray.size(); i++) {
                ActiveNode node = makeNodeInstance(jsonArray.get(i));
                SettingObjRepository.add(node.getId(), node);
            }

            connectBetweenNodes();
            startNodes();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    /**
     * 각 jsonElement(node 1개)의 type이 NodeGroup에 존재한다면 Node 객체를 생성하여 반환합니다.
     *
     * @param nodeInfo jsonElement인 node 정보
     * @throws NotExistsNodeException NodeGroup에 존재하지 않는 type인 경우
     * @return 생성된 Node 객체
     * @author 이수정
     */
    private static ActiveNode makeNodeInstance(JsonElement nodeInfo) {
        String type = nodeInfo.getAsJsonObject().get("type").getAsString();
        for (NodeGroup node : NodeGroup.values()) {
            if (type.equals(node.name())) {
                return node.makeNodeInstance(new Gson().fromJson(nodeInfo, NodeInfo.class));
            }
        }
        throw new NotExistsNodeException(type);
    }

    /**
     * 생성된 노드 간 wire를 연결합니다.
     * wire 연결 정보는 InAble 노드의 outWire에 저장되어 있습니다.
     *
     * @author 이수정
     */
    private static void connectBetweenNodes() {
        for (ActiveNode fromNode : SettingObjRepository.getNodeValues()) {
            if (fromNode instanceof InAble) {
                ((InAble) fromNode).connectBetweenNodes();
            }
        }
    }

    /**
     * 생성된 Node들을 실행시킵니다.
     *
     * @author 이수정
     */
    private static void startNodes() {
        for (ActiveNode node : SettingObjRepository.getNodeValues()) {
            node.start();
        }
    }
}
