package com.nhnacademy.aiotdevicegateway.node.base;

import com.nhnacademy.aiotdevicegateway.SettingObjRepository;
import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 데이터를 입력받아 wire로 OutAble 노드에 Message를 전달하는 Node class 입니다.
 *
 * @author 이수정
 */
@Slf4j
public abstract class InNode extends ActiveNode implements InAble {

    private final List<Wire> outWire;

    /**
     * 출력 와이어 정보를 바탕으로 wire 리스트를 생성하고 연결합니다.
     *
     * @param id    노드 ID
     * @param name  노드 이름
     * @param wires 연결될 출력 와이어 정보
     * @author 이수정
     */
    protected InNode(String id, String name, String[] wires) {
        super(id, name);
        outWire = new ArrayList<>();

        for (String wire : wires) {
            connectOutWire(new Wire(getId(), wire));
        }
    }

    @Override
    public void connectOutWire(Wire wire) {
        outWire.add(wire);
    }

    @Override
    public void connectOutWire(int index, Wire wire) {
        if (index < 0 || getOutWireLength() <= index) {
            throw new IllegalArgumentException();
        }
        log.debug("index {} wire : before {} -> after {}", index, outWire.get(index), wire);
        outWire.set(index, wire);
    }

    /**
     * 특정 index의 wire를 반환합니다.
     *
     * @param index 반환받을 wire의 index
     * @return 특정 index의 wire
     * @author 이수정
     */
    @Override
    public Wire getOutWire(int index) {
        return outWire.get(index);
    }

    /**
     * outWire의 길이를 반환합니다.
     *
     * @return outWire의 길이
     * @author 이수정
     */
    @Override
    public int getOutWireLength() {
        return outWire.size();
    }

    @Override
    public void connectBetweenNodes() {
        for (int i = 0; i < getOutWireLength(); i++) {
            Wire wire = getOutWire(i);
            ActiveNode toNode = SettingObjRepository.getNode(wire.getToId());
            if (toNode instanceof OutAble) {
                ((OutAble) toNode).connectInWire(wire);
            }
        }
    }

    @Override
    public void output(Message message) {
        for (int i = 0; i < getOutWireLength(); i++) {
            outWire.get(i).add(message);
        }
    }
}
