package com.jd.service;

import com.jd.dto.ContainerDTO;
import com.jd.exception.ContainerNotEmptyException;
import com.jd.exception.InsufficientCapacityException;
import com.jd.exception.ResourceNotFoundException;
import com.jd.model.Container;
import com.jd.repository.ContainerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContainerService {
    private static final Logger logger = LoggerFactory.getLogger(ContainerService.class);

    @Autowired
    private ContainerRepository containerRepository;

    private void validateContainerDTO(ContainerDTO dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Container name is required");
        }
        if (dto.getCapacity() == null || dto.getCapacity() < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1");
        }
    }


    @Transactional
    public Container addContainer(ContainerDTO containerDto) {
        validateContainerDTO(containerDto);
        Container container = new Container();
        container.setName(containerDto.getName());
        container.setCapacity(containerDto.getCapacity());
        container.setAvailableCapacity(containerDto.getCapacity()); // 设置初始可用容量
        return containerRepository.save(container);
    }

    @Transactional
    public ContainerDTO updateContainer(Integer id, ContainerDTO containerDto) {
        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Container not found with id: " + id));

        validateContainerDTO(containerDto);

        container.setName(containerDto.getName());

        int usedCapacity = container.getCapacity() - container.getAvailableCapacity();
        if (containerDto.getCapacity() < usedCapacity) {
            throw new IllegalArgumentException("New capacity is too small for current content");
        }

        container.setCapacity(containerDto.getCapacity());
        container.setAvailableCapacity(containerDto.getCapacity() - usedCapacity);

        Container updatedContainer = containerRepository.save(container);

        // 直接创建并返回 ContainerDTO
        ContainerDTO updatedDto = new ContainerDTO();
        updatedDto.setId(updatedContainer.getId());
        updatedDto.setName(updatedContainer.getName());
        updatedDto.setCapacity(updatedContainer.getCapacity());
        updatedDto.setAvailableCapacity(updatedContainer.getAvailableCapacity());

        return updatedDto;
    }


    public Container getContainerById(Integer id) {
        logger.info("Fetching container with id: {}", id);
        return containerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Container not found with id: " + id));
    }

    public List<Container> getAllContainers() {
        logger.info("Fetching all containers");
        return containerRepository.findAll();
    }

    @Transactional
    public void deleteContainer(Integer id) {
        logger.info("Deleting container with id: {}", id);
        Container container = containerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Container not found with id: " + id));

        if (container.getCapacity() != container.getAvailableCapacity()) {
            throw new ContainerNotEmptyException("Cannot delete container that is not empty");
        }

        containerRepository.delete(container);
    }
}
