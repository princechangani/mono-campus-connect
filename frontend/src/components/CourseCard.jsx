import React from 'react';
import {
    Card,
    CardContent,
    Typography,
    IconButton,
    Box,
} from '@mui/material';
import {
    Edit as EditIcon,
    Delete as DeleteIcon,
} from '@mui/icons-material';

const CourseCard = ({ course, onEdit, onDelete }) => {
    return (
        <Card sx={{ mb: 2 }}>
            <CardContent>
                <Typography variant="h6" component="div">
                    {course.courseName}
                </Typography>
                <Box sx={{ mt: 1, display: 'flex', gap: 2 }}>
                    <Typography variant="body2" color="text.secondary">
                        <strong>Code:</strong> {course.courseCode}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        <strong>Department:</strong> {course.department}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        <strong>Credits:</strong> {course.credits}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        <strong>Semester:</strong> {course.semester}
                    </Typography>
                </Box>
                <Box sx={{ mt: 2, display: 'flex', justifyContent: 'flex-end', gap: 1 }}>
                    <IconButton onClick={() => onEdit(course)} color="primary">
                        <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => onDelete(course.id)} color="error">
                        <DeleteIcon />
                    </IconButton>
                </Box>
            </CardContent>
        </Card>
    );
};

export default CourseCard;
