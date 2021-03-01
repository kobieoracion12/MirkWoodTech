package com.oopclass.breadapp.services.impl;

import com.oopclass.breadapp.models.Appointment;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oopclass.breadapp.models.Appointment;
import com.oopclass.breadapp.repository.AppointmentRepository;
import com.oopclass.breadapp.services.IAppointmentService;

/**
 * OOP Class 20-21
 * @author Gerald Villaran
 */

@Service
public class AppointmentService implements IAppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Override
	public Appointment save(Appointment entity) {
		return appointmentRepository.save(entity);
	}

	@Override
	public Appointment update(Appointment entity) {
		return appointmentRepository.save(entity);
	}

	@Override
	public void delete(Appointment entity) {
		appointmentRepository.delete(entity);
	}

	@Override
	public void delete(Long id) {
		appointmentRepository.deleteById(id);
	}

	@Override
	public Appointment find(Long id) {
		return appointmentRepository.findById(id).orElse(null);
	}

	@Override
	public List<Appointment> findAll() {
		return appointmentRepository.findAll();
	}

	@Override
	public void deleteInBatch(List<Appointment> appointments) {
		appointmentRepository.deleteInBatch(appointments);
	}
}
