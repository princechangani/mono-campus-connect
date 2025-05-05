import React, { useState } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Select,
    MenuItem,
    FormControl,
    InputLabel,
} from '@mui/material';

const CourseForm = ({ open, onClose, onSubmit, course }) => {
    const [formData, setFormData] = useState({
        courseCode: course?.courseCode || '',
        courseName: course?.courseName || '',
        department: course?.department || '',
        credits: course?.credits || '',
        instructor: course?.instructor || '',
        semester: course?.semester || '',
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
        onClose();
    };

    return (
        <Dialog open={open} onClose={onClose} maxWidth="sm" fullWidth>
            <DialogTitle>{course ? 'Edit Course' : 'Add New Course'}</DialogTitle>
            <form onSubmit={handleSubmit}>
                <DialogContent>
                    <TextField
                        margin="dense"
                        name="courseCode"
                        label="Course Code"
                        fullWidth
                        value={formData.courseCode}
                        onChange={handleChange}
                        required
                    />
                    <TextField
                        margin="dense"
                        name="courseName"
                        label="Course Name"
                        fullWidth
                        value={formData.courseName}
                        onChange={handleChange}
                        required
                    />
                    <FormControl margin="dense" fullWidth>
                        <InputLabel>Department</InputLabel>
                        <Select
                            name="department"
                            value={formData.department}
                            onChange={handleChange}
                            required
                        >
                            <MenuItem value="">Select Department</MenuItem>
                            <MenuItem value="CSE">CSE</MenuItem>
                            <MenuItem value="ECE">ECE</MenuItem>
                            <MenuItem value="MECH">MECH</MenuItem>
                            <MenuItem value="CIVIL">CIVIL</MenuItem>
                        </Select>
                    </FormControl>
                    <TextField
                        margin="dense"
                        name="credits"
                        label="Credits"
                        type="number"
                        fullWidth
                        value={formData.credits}
                        onChange={handleChange}
                        required
                    />
                    <TextField
                        margin="dense"
                        name="instructor"
                        label="Instructor"
                        fullWidth
                        value={formData.instructor}
                        onChange={handleChange}
                        required
                    />
                    <FormControl margin="dense" fullWidth>
                        <InputLabel>Semester</InputLabel>
                        <Select
                            name="semester"
                            value={formData.semester}
                            onChange={handleChange}
                            required
                        >
                            <MenuItem value="">Select Semester</MenuItem>
                            {[1, 2, 3, 4, 5, 6, 7, 8].map(sem => (
                                <MenuItem key={sem} value={sem}>
                                    Semester {sem}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>
                </DialogContent>
                <DialogActions>
                    <Button onClick={onClose}>Cancel</Button>
                    <Button type="submit" variant="contained" color="primary">
                        {course ? 'Update' : 'Add'}
                    </Button>
                </DialogActions>
            </form>
        </Dialog>
    );
};

export default CourseForm;
