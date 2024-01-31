package com.nhnacademy.aiotdevicegateway.node.base;

import com.nhnacademy.aiotdevicegateway.SettingObjRepository;
import com.nhnacademy.aiotdevicegateway.message.Message;
import com.nhnacademy.aiotdevicegateway.wire.Wire;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Inable노드에게서 전달받은 Message로 특정 작업을 실행하고,
 * OutAble 노드에 처리된 Message를 전달하는 Node class 입니다.
 *
 * @author 이수정
 */
@Slf4j
public abstract class InOutNode extends ActiveNode implements InAble, OutAble {

    private final List<Wire> inWire;
    private final List<Wire> outWire;

    /**
     * 출력 와이어 정보를 바탕으로 wire 리스트를 생성하고 연결합니다.
     * (inWire는 초기화)
     *
     * @param id    노드 ID
     * @param name  노드 이름
     * @wires 연결될 출력 와이어 정보
     * @author 이수정
     */
    protected InOutNode(String id, String name, String[] wires) {
        super(id, name);
        inWire = new ArrayList<>();
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
    public void connectInWire(Wire wire) {
        inWire.add(wire);
    }

    @Override
    public void connectInWire(int index, Wire wire) {
        if (index < 0 || getInWireLength() <= index) {
            throw new IllegalArgumentException();
        }
        log.debug("index {} inwire : before {} -> after {}", index, inWire.get(index), wire);
        inWire.set(index, wire);
    }

    @Override
    public void connectOutWire(int index, Wire wire) {
        if (index < 0 || getOutWireLength() <= index) {
            throw new IllegalArgumentException();
        }
        log.debug("index {} outwire : before {} -> after {}", index, outWire.get(index), wire);
        outWire.set(index, wire);
    }

    @Override
    public Wire getInWire(int index) {
        return inWire.get(index);
    }

    @Override
    public Wire getOutWire(int index) {
        return outWire.get(index);
    }

    @Override
    public int getInWireLength() {
        return inWire.size();
    }

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
